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

