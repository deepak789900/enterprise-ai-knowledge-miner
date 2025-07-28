import React from 'react';
import './highlight.css';

interface DocumentProps {
  document: {
    id: number;
    fileName: string;
    content: string;
    summary: string;
    keywords: string;
    entities: string;
  };
}

// Utility to escape special characters for regex
const escapeRegex = (word: string) =>
  word.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');

// Highlight logic
const highlightWords = (
  text: string,
  keywords: string[],
  entities: string[]
): React.ReactNode[] => {
  const keywordSet = new Set(keywords.map(k => k.toLowerCase()));
  const entitySet = new Set(entities.map(e => e.toLowerCase()));
  const combined = [...new Set([...keywordSet, ...entitySet])];

  if (combined.length === 0) return [text];

  const regex = new RegExp(`(${combined.map(escapeRegex).join('|')})`, 'gi');
  const parts = text.split(regex);

  return parts.map((part, i) => {
    const lower = part.toLowerCase();
    if (keywordSet.has(lower)) {
      return (
        <span key={i} className="highlight-keyword" title="Keyword">
          {part}
        </span>
      );
    } else if (entitySet.has(lower)) {
      return (
        <span key={i} className="highlight-entity" title="Entity">
          {part}
        </span>
      );
    } else {
      return <span key={i}>{part}</span>;
    }
  });
};

const DocumentCard: React.FC<DocumentProps> = ({ document }) => {
  const { fileName, content, summary, keywords, entities } = document;

  const keywordList =
    keywords?.split(',').map(k => k.trim()).filter(Boolean) || [];
  const entityList =
    entities?.split(',').map(e => e.trim()).filter(Boolean) || [];

  return (
    <div className="document-card">
      <h3 className="font-bold text-lg mb-1">{fileName}</h3>

      <p className="text-sm text-gray-600 mb-1">
        <strong>Summary:</strong>{' '}
        {summary?.trim() ? summary : <em>No summary</em>}
      </p>

      <p className="text-sm text-yellow-700 mb-1">
        <strong>Keywords:</strong>{' '}
        {keywordList.length > 0 ? keywordList.join(', ') : <em>None</em>}
      </p>

      <p className="text-sm text-blue-700 mb-1">
        <strong>Entities:</strong>{' '}
        {entityList.length > 0 ? entityList.join(', ') : <em>None</em>}
      </p>

      <div className="mt-3">
        <strong className="text-gray-700">Content Preview:</strong>
        <div className="content-preview">
          {highlightWords(content || '', keywordList, entityList)}
        </div>
      </div>
    </div>
  );
};

export default DocumentCard;
