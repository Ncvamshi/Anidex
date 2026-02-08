from sqlalchemy import Column, Integer, ForeignKey, TIMESTAMP, Float
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from app.database import Base


class Scan(Base):
    __tablename__ = "scans"

    id = Column(Integer, primary_key=True)

    user_id = Column(ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
    animal_id = Column(ForeignKey("animals.id", ondelete="CASCADE"), nullable=False)

    confidence = Column(Float, nullable=True)
    scanned_at = Column(TIMESTAMP, server_default=func.now())

    user = relationship("User", backref="scans")
    animal = relationship("Animal", backref="scans")
