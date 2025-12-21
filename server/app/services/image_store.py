import os
import shutil
from uuid import uuid4

UPLOAD_DIR = "uploads/user_images"

def save_image(file):
    os.makedirs(UPLOAD_DIR, exist_ok=True)
    filename = f"{uuid4()}.jpg"
    path = os.path.join(UPLOAD_DIR, filename)

    with open(path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    return path
