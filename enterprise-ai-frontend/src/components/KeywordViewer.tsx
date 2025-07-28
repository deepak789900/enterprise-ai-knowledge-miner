import React, { useState } from 'react';
import axios from 'axios';

interface KeywordViewerProps {
  content: string;
}

const KeywordViewer: React.FC<KeywordViewerProps> = ({ content }) => {
  const [keywords, setKeywords] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchKeywords = async () => {
    if (!content || content.trim() === "") {
      setError("Content is empty. Cannot extract keywords.");
      return;
    }

    // Toggle visibility without refetching
    if (visible) {
      setVisible(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await axios.post(
        "http://localhost:8000/keywords/",
        {
          text: content,
          top_k: 10
        },
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );

      const keywordList: string[] = (response.data.keywords || []).map((kw: any) => kw.keyword);
      setKeywords(keywordList);
      setVisible(true);
    } catch (err) {
      console.error("Failed to fetch keywords:", err);
      setError("Could not fetch keywords. Please check your backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mt-4">
      <button
        onClick={fetchKeywords}
        className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
        disabled={loading}
      >
        {loading ? "Loading..." : visible ? "Hide Keywords" : "View Keywords"}
      </button>

      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

      {visible && (
        <div className="mt-3 text-sm text-gray-800">
          {keywords.length > 0 ? (
            <ul className="list-disc list-inside">
              {keywords.map((keyword, idx) => (
                <li key={idx}>{keyword}</li>
              ))}
            </ul>
          ) : (
            <p>No keywords found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default KeywordViewer;
