import PyPDF2
import sys

def read_pdf(pdf_path):
    try:
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            num_pages = len(pdf_reader.pages)
            
            print(f"Total pages: {num_pages}\n")
            print("=" * 80)
            
            for page_num in range(num_pages):
                page = pdf_reader.pages[page_num]
                text = page.extract_text()
                print(f"\n--- Page {page_num + 1} ---\n")
                print(text)
                print("\n" + "=" * 80)
                
    except Exception as e:
        print(f"Error reading PDF: {e}")

if __name__ == "__main__":
    pdf_path = r"C:\Users\leond\Aa VIA\assignment_2\PRO2-Assignment2.pdf"
    read_pdf(pdf_path)
