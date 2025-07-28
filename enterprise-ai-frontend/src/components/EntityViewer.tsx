import React, { useState } from 'react';
import axios from 'axios';

interface EntityViewerProps {
  content: string;
}

const EntityViewer: React.FC<EntityViewerProps> = ({ content }) => {
  const [entities, setEntities] = useState<Record<string, any[]> | null>(null);
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchEntities = async () => {
    if (!content || content.trim() === "") {
      setError("Content is empty. Cannot extract entities.");
      return;
    }

    // Toggle visibility without refetching if already loaded
    if (visible) {
      setVisible(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const res = await axios.post(
        "http://localhost:8000/entities/",
        {
          text: content,
          filter_labels: [],
          unique_only: true,
          group_by_label: true,
          return_freq: true,
          sort_by_freq: true,
          return_spans: false,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      const data = res.data.entities || res.data;
      setEntities(data);
      setVisible(true);
    } catch (err) {
      console.error("Failed to fetch entities:", err);
      setError("Could not fetch named entities. Please check the backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mt-4">
      <button
        onClick={fetchEntities}
        className="px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
        disabled={loading}
      >
        {loading ? "Loading..." : visible ? "Hide Entities" : "View Entities"}
      </button>

      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

      {visible && (
        <div className="mt-3 space-y-2 text-sm text-gray-800">
          {entities && Object.keys(entities).length > 0 ? (
            Object.entries(entities).map(([label, entityList]) => (
              <div key={label}>
                <h4 className="font-semibold capitalize">{label}</h4>
                <ul className="list-disc list-inside ml-4">
                  {entityList.map((entity, idx) => (
                    <li key={idx}>
                      {entity?.text || '[Missing]'}
                      {entity?.freq !== undefined ? ` (${entity.freq})` : ""}
                    </li>
                  ))}
                </ul>
              </div>
            ))
          ) : (
            <p className="text-gray-500">No entities found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default EntityViewer;
