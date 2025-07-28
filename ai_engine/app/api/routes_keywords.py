import logging
import io
import csv
import nltk
from fastapi import APIRouter, HTTPException, Request
from pydantic import BaseModel
from sklearn.feature_extraction.text import TfidfVectorizer
from nltk.corpus import stopwords
from fastapi.responses import StreamingResponse

# Setup logger
logger = logging.getLogger(__name__)

# Ensure stopwords are available
nltk.download("stopwords")

router = APIRouter(prefix="/keywords")

class KeywordRequest(BaseModel):
    text: str
    top_k: int = 10  # Number of keywords to return

@router.post("/")
def extract_keywords(req: KeywordRequest, request: Request):
    logger.info("Received keyword extraction request.")
    logger.debug(f"Input text length: {len(req.text)} | Top K: {req.top_k}")

    try:
        stop_words = "english"
        vectorizer = TfidfVectorizer(stop_words=stop_words)
        X = vectorizer.fit_transform([req.text])
        scores = X.toarray()[0]

        feature_names = vectorizer.get_feature_names_out()
        scored_keywords = list(zip(feature_names, scores))
        sorted_keywords = sorted(scored_keywords, key=lambda x: x[1], reverse=True)
        top_keywords = sorted_keywords[:req.top_k]

        export_format = request.query_params.get("export")
        logger.info(f"Export format: {export_format or 'json'}")

        if export_format == "csv":
            logger.info("Preparing CSV response for keywords.")
            output = io.StringIO()
            writer = csv.writer(output)
            writer.writerow(["Keyword", "TF-IDF Score"])
            for word, score in top_keywords:
                writer.writerow([word, round(score, 5)])
            output.seek(0)
            return StreamingResponse(output, media_type="text/csv", headers={
                "Content-Disposition": "attachment; filename=keywords.csv"
            })

        logger.info("Returning keyword extraction result as JSON.")
        return {"keywords": [{"keyword": word, "score": float(score)} for word, score in top_keywords]}

    except Exception as e:
        logger.exception("Error occurred during keyword extraction.")
        raise HTTPException(status_code=500, detail=str(e))
