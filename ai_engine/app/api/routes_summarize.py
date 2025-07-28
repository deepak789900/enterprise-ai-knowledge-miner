import logging
from fastapi import APIRouter, HTTPException, Query
from fastapi.responses import PlainTextResponse, StreamingResponse
from pydantic import BaseModel
from app.models.schemas import TextRequest, SummaryResponse
from app.services.summarizer import generate_summary
import csv
import io

# Initialize logger
logger = logging.getLogger(__name__)

# Add prefix so the endpoint becomes /summarize
router = APIRouter(prefix="/summarize")

@router.post("/", response_model=SummaryResponse)
def summarize(
    request: TextRequest,
    export: str = Query(None, description="Optional export format: txt or csv")
):
    logger.info("Received summarization request.")
    logger.debug(f"Input text length: {len(request.text)} characters")

    try:
        summary = generate_summary(request.text)
        logger.info("Summary generated successfully.")

        if export == "txt":
            logger.info("Export format requested: txt")
            return PlainTextResponse(content=summary, media_type="text/plain")

        elif export == "csv":
            logger.info("Export format requested: csv")
            output = io.StringIO()
            writer = csv.writer(output)
            writer.writerow(["summary"])
            writer.writerow([summary])
            output.seek(0)
            return StreamingResponse(output, media_type="text/csv", headers={
                "Content-Disposition": "attachment; filename=summary.csv"
            })

        logger.info("Returning summary as JSON.")
        return SummaryResponse(summary=summary)

    except Exception as e:
        logger.exception("Error occurred during summarization.")
        raise HTTPException(status_code=500, detail=str(e))
