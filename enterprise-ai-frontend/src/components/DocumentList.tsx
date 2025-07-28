import React, { useState } from 'react';
import type { Document } from '../types/Document';
import SearchBar from './SearchBar';
import '../assets/highlight.css';

interface DocumentListProps {
  documents: Document[];
  onSearchResults: (docs: Document[]) => void;
  onReset: () => void;
}

const extractEntityTexts = (entitiesRaw: string): string[] => {
  return entitiesRaw?.split(',').map(e => e.trim()).filter(Boolean) || [];
};

const highlightText = (text: string, keywords: string[], entities: string[]) => {
  const words = text.split(/(\s+)/);
  const keywordSet = new Set(keywords.map(k => k.toLowerCase()));
  const entitySet = new Set(entities.map(e => e.toLowerCase()));

  return (
    <>
      {words.map((word, i) => {
        const clean = word.replace(/[^\p{L}\p{N}_]/gu, '').toLowerCase();
        if (keywordSet.has(clean)) return <span key={i} className="highlight-keyword">{word}</span>;
        if (entitySet.has(clean)) return <span key={i} className="highlight-entity">{word}</span>;
        return <span key={i}>{word}</span>;
      })}
    </>
  );
};

const DocumentList: React.FC<DocumentListProps> = ({ documents, onSearchResults, onReset }) => {
  const [searching, setSearching] = useState(false);
  const [visibleKeywords, setVisibleKeywords] = useState<Record<number, boolean>>({});
  const [visibleEntities, setVisibleEntities] = useState<Record<number, boolean>>({});

  const handleSearch = async (query: string) => {
    setSearching(true);
    try {
      if (query.trim() === '') {
        onReset();
        return;
      }
      const res = await fetch(`http://localhost:8080/api/documents/search?q=${encodeURIComponent(query)}`);
      const data = await res.json();
      onSearchResults(data);
    } catch (err) {
      console.error(err);
    } finally {
      setSearching(false);
    }
  };

  const toggleKeywords = (id: number) => {
    setVisibleKeywords(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const toggleEntities = (id: number) => {
    setVisibleEntities(prev => ({ ...prev, [id]: !prev[id] }));
  };

  return (
    <div className="p-6">
      <SearchBar onSearch={handleSearch} />
      <h2 className="text-xl font-semibold mb-4">Documents</h2>

      {searching && <p>Searching...</p>}
      {!searching && documents.length === 0 && <p>No documents found.</p>}

      <ul className="space-y-6">
        {documents.map(doc => {
          const keywordList = doc.keywords?.split(',').map(k => k.trim()).filter(Boolean) || [];
          const entityList = extractEntityTexts(doc.entities);
          return (
            <li key={doc.id} className="border p-4 rounded shadow bg-white">
              <h3 className="font-bold">{doc.fileName}</h3>
              <p className="text-sm text-gray-600">Created: {new Date(doc.createdAt).toLocaleString()}</p>
              <p className="mt-2">{highlightText(doc.summary, keywordList, entityList)}</p>

              <div className="mt-3">
                <button onClick={() => toggleKeywords(doc.id)} className="mr-2 text-yellow-700">
                  {visibleKeywords[doc.id] ? 'Hide Keywords' : 'View Keywords'}
                </button>
                <button onClick={() => toggleEntities(doc.id)} className="text-blue-700">
                  {visibleEntities[doc.id] ? 'Hide Entities' : 'View Entities'}
                </button>
              </div>

                {visibleKeywords[doc.id] && (
                  <div className="mt-2">
                    <p className="text-yellow-800 text-sm font-semibold mb-1">Keywords:</p>
                    <div className="flex flex-wrap gap-2">
                      {keywordList.map((k, i) => (
                        <span key={i} className="bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full text-xs">
                          {k}
                        </span>
                      ))}
                    </div>
                  </div>
                )}

                {visibleEntities[doc.id] && (
                  <div className="mt-2">
                    <p className="text-blue-800 text-sm font-semibold mb-1">Entities:</p>
                    {entityList.length > 0 ? (
                      <div className="flex flex-wrap gap-2">
                        {entityList.map((e, i) => (
                          <span key={i} className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs">
                            {e}
                          </span>
                        ))}
                      </div>
                    ) : (
                      <p className="italic text-gray-400">None</p>
                    )}
                  </div>
                )}
            </li>
          );
        })}
      </ul>
    </div>
  );
};

export default DocumentList;
