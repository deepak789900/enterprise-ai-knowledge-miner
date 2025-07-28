# 🧠 Enterprise AI Knowledge Miner & Recommendation System

An AI-powered system designed to extract, summarize, and index knowledge from enterprise documents. It enables fast, intelligent document retrieval using advanced NLP techniques, keyword extraction, named entity recognition (NER), and search indexing via Elasticsearch.

---

## 🎯 Project Aim

To develop an intelligent enterprise document mining system that can:
- Automatically extract meaningful insights from uploaded files
- Provide summaries, keywords, and named entities
- Allow full-text document search
- Serve as a foundation for document-based knowledge management

---

## ✅ Objectives

- ✅ Upload and parse documents (PDF, DOCX, TXT)
- ✅ Extract content and summarize using AI
- ✅ Extract top keywords using TF-IDF
- ✅ Extract named entities using NLP (NER)
- ✅ Store documents and metadata in PostgreSQL
- ✅ Index documents in Elasticsearch for full-text search
- ✅ Search documents by file name, content, and summary
- ✅ Provide clean, modular architecture (Java + Python)
- ⏳ Build intuitive frontend in React (in progress)

---

## 🛠️ Tech Stack

| Component        | Technology                              |
|------------------|------------------------------------------|
| Backend API      | Java 17, Spring Boot                     |
| AI/NLP Engine    | Python 3.10+, FastAPI, spaCy, NLTK       |
| File Parsing     | Apache Tika                              |
| AI Summarization | HuggingFace Transformers                 |
| Keyword Extraction | TF-IDF (scikit-learn)                  |
| NER              | spaCy                                   |
| Search Engine    | Elasticsearch 8.x                        |
| Database         | PostgreSQL                               |
| Frontend         | React (in progress)                      |
| Build Tools      | Maven, pip                               |
| API Format       | RESTful APIs                             |
| Deployment       | Localhost (Docker optional)              |

---

## 🔧 Setup Instructions

### Prerequisites

- Java 17+
- Python 3.10+
- Node.js (for frontend, optional)
- PostgreSQL 15+
- Elasticsearch 8.x

---

## ⚙️ Backend Setup (Java Spring Boot)

### 1. Configure Database and Elasticsearch

Edit the `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/enterprise_ai
spring.datasource.username=your_username
spring.datasource.password=your_password

elasticsearch.host=http://localhost:9200
ai.engine.base-url=http://localhost:8000

Build and Run
bash
cd enterprise-ai-knowledge-miner/java-backend
./mvnw clean install        # or: mvn clean install
./mvnw spring-boot:run      # or: mvn spring-boot:run


🧠 AI Engine Setup (Python FastAPI)
1. Create and Activate Virtual Environment
bash

cd enterprise-ai-knowledge-miner/ai_engine
python -m venv venv

# Activate venv:
# On Windows:
venv\Scripts\activate
# On macOS/Linux:
source venv/bin/activate

2. Install Dependencies
bash
pip install -r requirements.txt
3. Run the AI Engine
bash

uvicorn app.main:app --reload --port 8000
