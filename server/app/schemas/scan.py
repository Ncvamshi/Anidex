from pydantic import BaseModel
from datetime import datetime

class ScanOut(BaseModel):
    animal_id: int
    confidence: float
    image_path: str
    scanned_at: datetime

    class Config:
        orm_mode = True
