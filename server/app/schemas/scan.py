from pydantic import BaseModel
from datetime import datetime
from app.schemas.animal import AnimalOut


class ScanOut(BaseModel):
    id: int
    confidence: float | None = None
    scanned_at: datetime
    animal: AnimalOut

    class Config:
        from_attributes = True
