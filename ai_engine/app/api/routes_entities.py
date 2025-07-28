# app/api/routes_entities.py

from fastapi import APIRouter, HTTPException, Response, Query
from pydantic import BaseModel
from typing import List, Optional, Dict
import spacy
from collections import defaultdict
import csv
import io
import logging

# Setup logger
logger = logging.getLogger(__name__)

# Load spaCy model
nlp = spacy.load("en_core_web_sm")
router = APIRouter()

# Request model
class EntityRequest(BaseModel):
    text: str
    labels: Optional[List[str]] = None
    unique: Optional[bool] = False

# Label to readable mapping
label_mapping = {
    "PERSON": "Person",
    "ORG": "Organization",
    "GPE": "Location (GPE)",
    "LOC": "Location",
    "DATE": "Date",
    "TIME": "Time",
    "MONEY": "Monetary Value",
    "NORP": "Group/Nationality",
    "FAC": "Facility",
    "PRODUCT": "Product",
    "EVENT": "Event",
    "WORK_OF_ART": "Work of Art",
    "LAW": "Law",
    "LANGUAGE": "Language"
}

@router.post("/entities/")
def extract_entities(
    request: EntityRequest,
    format: str = Query("json", enum=["json", "csv"])
):
    logger.info("📥 Received NER request.")
    logger.debug(f"Text length: {len(request.text)} | Labels: {request.labels} | Unique: {request.unique} | Format: {format}")

    try:
        if not request.text.strip():
            logger.warning("⚠️ Empty text content received.")
            raise HTTPException(status_code=400, detail="Text content is empty.")

        doc = nlp(request.text)
        grouped_entities: Dict[str, List[Dict[str, str]]] = defaultdict(list)
        seen = set()

        for ent in doc.ents:
            if request.labels and ent.label_ not in request.labels:
                continue

            entity_text = ent.text.strip()
            entity_label = ent.label_

            if not entity_text:
                continue

            if request.unique:
                key = (entity_text.lower(), entity_label)
                if key in seen:
                    continue
                seen.add(key)

            grouped_entities[entity_label].append({
                "text": entity_text
            })

        if format == "json":
            logger.info("✅ Returning grouped JSON entities.")
            return grouped_entities

        logger.info("📄 Returning entity data as CSV.")
        output = io.StringIO()
        writer = csv.writer(output)
        writer.writerow(["label", "text"])

        for label, entities in grouped_entities.items():
            for item in entities:
                writer.writerow([label_mapping.get(label, label), item["text"]])

        return Response(
            content=output.getvalue(),
            media_type="text/csv",
            headers={"Content-Disposition": "attachment; filename=entities.csv"}
        )

    except Exception as e:
        logger.exception("❌ Entity extraction failed.")
        raise HTTPException(status_code=500, detail=str(e))
