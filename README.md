# 🧠 Enterprise AI Knowledge Miner & Recommendation System

An intelligent system that parses uploaded documents, generates summaries, extracts keywords and named entities, indexes them into Elasticsearch, and provides full-text search — all powered by AI/NLP.

## 🚀 Features

- Upload documents (PDF, DOCX, TXT)
- Automatically:
  - Parse file content
  - Generate AI summary
  - Extract top keywords (via TF-IDF)
  - Identify named entities (NER)
- Store metadata in PostgreSQL
- Index documents in Elasticsearch for search
- Search by file name, content, or summary
- Built-in deduplication and CRUD operations
- Frontend UI (React) [Coming soon...]

---

## 🛠️ Tech Stack

| Layer           | Technology                                |
|----------------|--------------------------------------------|
| Backend         | Java + Spring Boot                        |
| AI/NLP Engine   | Python + FastAPI (HuggingFace, spaCy, NLTK)|
| Database        | PostgreSQL                                |
| Search Engine   | Elasticsearch                             |
| Frontend        | React.js (in progress)                    |
| Build Tools     | Maven, pip                                |
| Deployment      | Docker (optional), GitHub                 |

---

## 🗂️ Project Structure

enterprise-ai-knowledge-miner/
│
├── ai_engine/ # FastAPI-based AI microservice
│ ├── app/
│ │ ├── api/
│ │ │ ├── routes_summarize.py
│ │ │ ├── routes_keywords.py
│ │ │ └── routes_entities.py
│ │ ├── main.py
│ │ └── utils/
│ └── requirements.txt
│
├── ai_backend/ # Java Spring Boot backend
│ ├── src/
│ │ ├── main/java/com/enterprise/ai_backend/
│ │ │ ├── controller/
│ │ │ ├── service/
│ │ │ ├── repository/
│ │ │ ├── model/
│ │ │ └── dto/
│ │ └── resources/
│ │ └── application.properties
│ └── pom.xml
│
├── frontend/ # React frontend (WIP)
│
├── docker-compose.yml (optional)
└── README.md


---

## ⚙️ Backend Setup (Spring Boot)

### Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Elasticsearch (v8.x)

### Steps

```bash
# 1. Navigate to backend
cd ai_backend

# 2. Update PostgreSQL credentials in:
# src/main/resources/application.properties

# 3. Build and run the application
mvn spring-boot:run

# 1. Navigate to AI engine
cd ai_engine

# 2. Create virtual environment
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# 3. Install dependencies
pip install -r requirements.txt

# 4. Run the FastAPI server
uvicorn app.main:app --reload --port 8000


