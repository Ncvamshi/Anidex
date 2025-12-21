from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..database import get_db
from ..models.animal import Animal

router = APIRouter(prefix="/animals", tags=["Animals"])

@router.get("/")
def get_all_animals(db: Session = Depends(get_db)):
    return db.query(Animal).all()

@router.get("/{name}")
def get_animal(name: str, db: Session = Depends(get_db)):
    return db.query(Animal).filter(Animal.name == name).first()
