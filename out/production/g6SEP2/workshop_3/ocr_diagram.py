import fitz
import pytesseract
from PIL import Image
import io

# Try to find tesseract
import os
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

doc = fitz.open(r'C:\Users\leond\Aa VIA\workshop_3\src\mvvm_excercises\document.pdf')
page = doc[1]
pix = page.get_pixmap(dpi=300)
img_data = pix.tobytes("png")
img = Image.open(io.BytesIO(img_data))
text = pytesseract.image_to_string(img)
print(text)

