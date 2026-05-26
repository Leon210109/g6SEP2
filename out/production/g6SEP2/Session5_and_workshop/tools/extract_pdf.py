import sys
from pathlib import Path

try:
    from PyPDF2 import PdfReader
except Exception as e:
    print("MISSING_PYPDF2")
    sys.exit(2)

if len(sys.argv) < 2:
    print("Usage: python extract_pdf.py <pdf-path>")
    sys.exit(1)

pdf_path = Path(sys.argv[1])
if not pdf_path.exists():
    print(f"File not found: {pdf_path}")
    sys.exit(1)

reader = PdfReader(str(pdf_path))
text = []
for p in reader.pages:
    t = p.extract_text()
    if t:
        text.append(t)

print("\n".join(text))
