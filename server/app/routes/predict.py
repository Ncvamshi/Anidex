from fastapi import APIRouter, UploadFile, File, Depends, HTTPException
from sqlalchemy.orm import Session
import os
import shutil
from uuid import uuid4

from app.database import get_db
from app.services.predictor import predict_animal
from app.models.animal import Animal
from app.models.scan import Scan

router = APIRouter(prefix="/predict", tags=["Prediction"])

UPLOAD_DIR = "uploads/user_images"


@router.post("/")
def predict(
    file: UploadFile = File(...),
    user_id: str = None,   # TEMP: pass user_id manually for now
    db: Session = Depends(get_db)
):
    # 1. Save image
    os.makedirs(UPLOAD_DIR, exist_ok=True)

    filename = f"{uuid4()}_{file.filename}"
    image_path = os.path.join(UPLOAD_DIR, filename)

    with open(image_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    # 2. Predict animal
    predicted_name, confidence = predict_animal(image_path)

    if predicted_name == "Unknown":
        raise HTTPException(
            status_code=400,
            detail="Could not identify animal"
        )

    # 3. Fetch animal from DB
    animal = db.query(Animal).filter(Animal.name == predicted_name).first()

    if not animal:
        raise HTTPException(
            status_code=404,
            detail=f"Animal '{predicted_name}' not found in database"
        )

    # 4. Save scan (collection logic)
    scan = Scan(
        user_id=user_id,
        animal_id=animal.id,
        confidence=confidence,
        image_path=image_path
    )

    db.add(scan)
    db.commit()

    # 5. Return Pokédex data
    return {
        "animal": {
            "id": animal.id,
            "name": animal.name,
            "scientific_name": animal.scientific_name,
            "category": animal.category,
            "description": animal.description,
            "max_height": animal.max_height,
            "max_weight": animal.max_weight,
            "diet": animal.diet,
            "habitat": animal.habitat,
            "colors": animal.colors,
            "lifestyle": animal.lifestyle,
            "fun_fact": animal.fun_fact
        },
        "confidence": confidence
    }
