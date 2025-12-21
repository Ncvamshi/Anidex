from pydantic import BaseModel
from typing import List, Optional

class AnimalOut(BaseModel):
    id: int
    name: str
    scientific_name: Optional[str]
    category: Optional[str]
    description: Optional[str]
    max_height: Optional[str]
    max_weight: Optional[str]
    diet: Optional[str]
    habitat: Optional[str]
    breeds: Optional[List[str]]
    colors: Optional[List[str]]
    lifestyle: Optional[str]
    fun_fact: Optional[str]

    class Config:
        orm_mode = True
