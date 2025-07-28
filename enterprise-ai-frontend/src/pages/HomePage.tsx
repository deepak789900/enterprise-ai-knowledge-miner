import React, { useEffect, useState } from 'react';
import UploadForm from '../components/UploadForm';
import DocumentList from '../components/DocumentList';
import type { Document } from '../types/Document';

const HomePage: React.FC = () => {
  const [documents, setDocuments] = useState<Document[]>([]);

  const fetchDocuments = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/documents');
      const data = await res.json();
      setDocuments(data);
    } catch (err) {
      console.error('Failed to fetch documents:', err);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, []);

  const handleUploadSuccess = () => {
    fetchDocuments();
  };

  const handleSearchResults = (filteredDocs: Document[]) => {
    setDocuments(filteredDocs);
  };

  const handleReset = () => {
    fetchDocuments();
  };

  const existingFileNames = documents.map(doc => doc.fileName);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Enterprise AI Knowledge Miner</h1>
      <UploadForm onUploadSuccess={handleUploadSuccess} existingFileNames={existingFileNames} />
      <DocumentList documents={documents} onSearchResults={handleSearchResults} onReset={handleReset} />
    </div>
  );
};

export default HomePage;
