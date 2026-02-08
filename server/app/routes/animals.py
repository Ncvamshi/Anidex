from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from sqlalchemy import or_, func

from app.database import get_db
from app.models.animal import Animal
from app.schemas.animal import AnimalOut

router = APIRouter(prefix="/animals", tags=["Animals"])


@router.get("/", response_model=list[AnimalOut])
def get_all_animals(db: Session = Depends(get_db)):
    return db.query(Animal).all()


@router.get("/search", response_model=list[AnimalOut])
def search_animals(
    q: str = Query(..., min_length=1),
    db: Session = Depends(get_db),
):
    q = q.strip()

    # ✅ 1) Exact match on full Animal.name -> return only that one
    exact_name = (
        db.query(Animal)
        .filter(func.lower(Animal.name) == q.lower())
        .first()
    )
    if exact_name:
        return [exact_name]

    # ✅ 2) Exact match on common_name -> return all animals with that common_name
    exact_common = (
        db.query(Animal)
        .filter(func.lower(func.coalesce(Animal.common_name, "")) == q.lower())
        .all()
    )
    if exact_common:
        return exact_common

    # ✅ 3) Fallback: partial search
    results = (
        db.query(Animal)
        .filter(
            or_(
                Animal.name.ilike(f"%{q}%"),
                func.coalesce(Animal.common_name, "").ilike(f"%{q}%"),
                func.coalesce(Animal.scientific_name, "").ilike(f"%{q}%"),
                func.coalesce(Animal.category, "").ilike(f"%{q}%"),
            )
        )
        .all()
    )

    return results


@router.get("/{animal_id}", response_model=AnimalOut)
def get_animal_by_id(animal_id: int, db: Session = Depends(get_db)):
    animal = db.query(Animal).filter(Animal.id == animal_id).first()
    if not animal:
        raise HTTPException(status_code=404, detail="Animal not found")
    return animal


@router.get("/name/{name}", response_model=AnimalOut)
def get_animal_by_name(name: str, db: Session = Depends(get_db)):
    animal = db.query(Animal).filter(Animal.name == name).first()
    if not animal:
        raise HTTPException(status_code=404, detail="Animal not found")
    return animal
