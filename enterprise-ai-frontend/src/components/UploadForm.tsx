import React, { useState } from 'react';
import axios from 'axios';

interface UploadFormProps {
  onUploadSuccess: () => void;
  existingFileNames: string[];
}

const UploadForm: React.FC<UploadFormProps> = ({ onUploadSuccess, existingFileNames }) => {
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState('');
  const [uploading, setUploading] = useState(false);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFile(event.target.files?.[0] || null);
    setMessage('');
  };

  const handleUpload = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!file) {
      setMessage('⚠️ Please select a file.');
      return;
    }

    const selectedName = file.name.toLowerCase();
    const alreadyExists = existingFileNames.some(name => name.toLowerCase() === selectedName);
    if (alreadyExists) {
      setMessage(`⚠️ A file named "${file.name}" already exists.`);
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    setUploading(true);
    setMessage('Uploading...');

    try {
      await axios.post('http://localhost:8080/api/upload', formData);
      setMessage('✅ Upload successful!');
      setFile(null);
      onUploadSuccess();
    } catch (err) {
      console.error(err);
      setMessage('❌ Upload failed.');
    } finally {
      setUploading(false);
    }
  };

  return (
    <form onSubmit={handleUpload}>
      <input type="file" accept=".txt,.pdf,.docx,.md" onChange={handleFileChange} disabled={uploading} />
      <button type="submit" disabled={!file || uploading}>Upload</button>
      <p>{message}</p>
    </form>
  );
};

export default UploadForm;
