from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.database import get_db
from app.models.user import User
from app.models.scan import Scan
from app.schemas.scan import ScanOut

router = APIRouter(prefix="/collection", tags=["Collection"])


@router.get("/{user_id}", response_model=list[ScanOut])
def get_user_collection(user_id: str, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    scans = (
        db.query(Scan)
        .filter(Scan.user_id == user_id)
        .order_by(Scan.scanned_at.desc())
        .all()
    )

    return scans
