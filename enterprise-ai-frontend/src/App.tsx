import React, { useEffect, useState, useCallback } from 'react';
import './App.css';
import HomePage from './pages/HomePage';
import axios from 'axios';

interface Document {
  id: number;
  fileName: string;
  createdAt: string;
  summary: string;
  content: string;
  keywords: string;
  entities: string;
}

function App() {
  const [documents, setDocuments] = useState<Document[]>([]);

  const fetchDocuments = useCallback(async () => {
    try {
      const res = await axios.get<Document[]>('http://localhost:8080/api/documents');

      // ✅ Filter duplicates by file name (case-insensitive)
      const uniqueDocs = Array.from(
        new Map(res.data.map(doc => [doc.fileName.toLowerCase(), doc])).values()
      );

      setDocuments(uniqueDocs);
    } catch (error) {
      console.error('❌ Failed to fetch documents:', error);
    }
  }, []);

  useEffect(() => {
    fetchDocuments();
  }, [fetchDocuments]);

  return (
    <div className="App">
      <HomePage
        documents={documents}
        onRefreshDocuments={fetchDocuments} // ✅ Matches expected prop in HomePageProps
        onSearchResults={setDocuments}
      />
    </div>
  );
}

export default App;
