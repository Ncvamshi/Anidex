from sqlalchemy import Column, Integer, String, Text
from sqlalchemy.dialects.postgresql import JSONB
from app.database import Base

class Animal(Base):
    __tablename__ = "animals"

    id = Column(Integer, primary_key=True)
    name = Column(String, unique=True, nullable=False)
    scientific_name = Column(String)
    category = Column(String)
    description = Column(Text)
    max_height = Column(String)
    max_weight = Column(String)
    diet = Column(String)
    habitat = Column(String)
    breeds = Column(JSONB)
    colors = Column(JSONB)
    lifestyle = Column(String)
    fun_fact = Column(Text)
