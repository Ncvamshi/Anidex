from pydantic import BaseModel
from typing import List, Optional


class AnimalOut(BaseModel):
    id: int
    name: str
    common_name: Optional[str] = None
    scientific_name: Optional[str] = None
    category: Optional[str] = None
    description: Optional[str] = None

    max_height: Optional[str] = None
    max_weight: Optional[str] = None
    diet: Optional[str] = None
    habitat: Optional[str] = None

    breeds: Optional[List[str]] = None
    colors: Optional[List[str]] = None
    lifestyle: Optional[str] = None
    fun_fact: Optional[str] = None

    image_url: Optional[str] = None

    class Config:
        from_attributes = True
