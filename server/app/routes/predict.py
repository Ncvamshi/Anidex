# from fastapi import APIRouter, UploadFile, File, Depends, HTTPException
# from sqlalchemy.orm import Session

# from app.database import get_db
# from app.services.predictor import predict_animal
# from app.models.animal import Animal
# from app.models.scan import Scan
# from app.models.user import User
# from app.schemas.animal import AnimalOut

# router = APIRouter(prefix="/predict", tags=["Prediction"])


# @router.post("/")
# async def predict(
#     file: UploadFile = File(...),
#     user_id: str | None = None,  # TEMP: pass user_id manually from Android
#     db: Session = Depends(get_db),
# ):
#     if not user_id:
#         raise HTTPException(status_code=400, detail="user_id is required")

#     user = db.query(User).filter(User.id == user_id).first()
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")

#     image_bytes = await file.read()
#     predicted_name, confidence = predict_animal(image_bytes)

#     if predicted_name == "Unknown":
#         raise HTTPException(status_code=400, detail="Could not identify animal")

#     animal = db.query(Animal).filter(Animal.name == predicted_name).first()
#     if not animal:
#         raise HTTPException(
#             status_code=404,
#             detail=f"Animal '{predicted_name}' not found in database",
#         )

#     # ✅ Save scan = user collected animal
#     scan = Scan(
#         user_id=user_id,
#         animal_id=animal.id,
#         confidence=confidence,
#     )
#     db.add(scan)
#     db.commit()
#     db.refresh(scan)

#     return {
#         "animal": AnimalOut.model_validate(animal).model_dump(),
#         "confidence": confidence,
#         "scan_id": scan.id,
#         "scanned_at": scan.scanned_at,
#     }


from fastapi import APIRouter, UploadFile, File, Depends, HTTPException
from sqlalchemy.orm import Session

from app.database import get_db
from app.services.predictor import predict_animal
from app.models.scan import Scan
from app.models.user import User
from app.schemas.animal import AnimalOut

router = APIRouter(prefix="/predict", tags=["Prediction"])


@router.post("/")
async def predict(
    file: UploadFile = File(...),
    user_id: str | None = None,  # TEMP: pass user_id manually from Android
    db: Session = Depends(get_db),
):
    if not user_id:
        raise HTTPException(status_code=400, detail="user_id is required")

    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    image_bytes = await file.read()

    animal, confidence, vision_label = predict_animal(image_bytes, db)

    if not animal:
        raise HTTPException(
            status_code=400,
            detail=f"Could not map Vision label '{vision_label}' to any animal in DB",
        )

    # ✅ Prevent duplicate collection (same animal for same user)
    existing = (
        db.query(Scan)
        .filter(Scan.user_id == user_id, Scan.animal_id == animal.id)
        .first()
    )

    if existing:
        # Optionally keep the best confidence
        if confidence > (existing.confidence or 0):
            existing.confidence = confidence
            db.commit()
            db.refresh(existing)

        return {
            "already_collected": True,
            "scan_id": existing.id,
            "scanned_at": existing.scanned_at,
            "confidence": existing.confidence,
            "vision_label": vision_label,
            "animal": AnimalOut.model_validate(animal).model_dump(),
        }

    scan = Scan(
        user_id=user_id,
        animal_id=animal.id,
        confidence=confidence,
    )
    db.add(scan)
    db.commit()
    db.refresh(scan)

    return {
        "already_collected": False,
        "scan_id": scan.id,
        "scanned_at": scan.scanned_at,
        "confidence": confidence,
        "vision_label": vision_label,
        "animal": AnimalOut.model_validate(animal).model_dump(),
    }
