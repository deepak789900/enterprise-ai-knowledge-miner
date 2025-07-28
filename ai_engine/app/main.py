import logging
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import routes_summarize, routes_keywords, routes_entities

# ✅ Configure logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ✅ Initialize FastAPI app
app = FastAPI()

# ✅ CORS configuration: allow React (5173) and Java backend (8080)
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",  # React frontend
        "http://localhost:8080"   # Java Spring Boot
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ✅ Log app startup
@app.on_event("startup")
async def startup_event():
    logger.info("🚀 FastAPI app has started.")

# ✅ Log app shutdown
@app.on_event("shutdown")
async def shutdown_event():
    logger.info("🛑 FastAPI app is shutting down.")

# ✅ Include API routers
app.include_router(routes_summarize.router)
app.include_router(routes_keywords.router)
app.include_router(routes_entities.router)
