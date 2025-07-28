from transformers import pipeline

summarizer = pipeline("summarization", model="facebook/bart-large-cnn")

def generate_summary(text: str) -> str:
    input_length = len(text.split())

    # Ensure max_length is never more than input tokens
    if input_length < 10:
        max_len = input_length  # don't over-summarize tiny text
    else:
        max_len = max(10, int(input_length * 0.6))

    summary = summarizer(
        text,
        max_length=max_len,
        min_length=5,
        do_sample=False
    )
    return summary[0]['summary_text']
