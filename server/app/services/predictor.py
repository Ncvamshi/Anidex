# from google.cloud import vision
# import io

# # Initialize Vision client once
# client = vision.ImageAnnotatorClient()

# # Map Google Vision labels to YOUR DB animal names
# LABEL_MAP = {
#     "Tiger": "Bengal Tiger",
#     "Elephant": "Indian Elephant",
#     "Dog": "Indian Pariah Dog",
#     "Peacock": "Indian Peafowl",
#     "Leopard": "Leopard"
# }

# def predict_animal(image_path: str):
#     """
#     Takes an image path
#     Returns: (mapped_animal_name, confidence)
#     """

#     with io.open(image_path, "rb") as image_file:
#         content = image_file.read()

#     image = vision.Image(content=content)

#     response = client.label_detection(image=image)
#     labels = response.label_annotations

#     if not labels:
#         return "Unknown", 0.0

#     # Take the top label
#     top_label = labels[0].description
#     confidence = labels[0].score

#     # Map to DB name
#     mapped_name = LABEL_MAP.get(top_label, "Unknown")

#     return mapped_name, float(confidence)


import re
from google.cloud import vision
from sqlalchemy.orm import Session
from app.models.animal import Animal

client = vision.ImageAnnotatorClient()

# ✅ Google Vision label -> Your DB animal.name
SYNONYMS = {
    "tiger": "Bengal Tiger",
    "bengal tiger": "Bengal Tiger",
    "indian tiger": "Bengal Tiger",

    "elephant": "Indian Elephant",
    "asian elephant": "Indian Elephant",
    "indian elephant": "Indian Elephant",

    "peacock": "Indian Peacock",
    "indian peafowl": "Indian Peacock",
    "peafowl": "Indian Peacock",

    "leopard": "Indian Leopard",
    "indian leopard": "Indian Leopard",

    "lion": "Asiatic Lion",
    "asiatic lion": "Asiatic Lion",

    "crocodile": "Mugger Crocodile",
    "mugger crocodile": "Mugger Crocodile",
    "gharial": "Gharial",

    "cobra": "Indian Cobra",
    "indian cobra": "Indian Cobra",
    "king cobra": "King Cobra",

    "python": "Indian Python",
    "indian python": "Indian Python",

    "bear": "Sloth Bear",
    "sloth bear": "Sloth Bear",

    "rhinoceros": "Indian Rhinoceros",
    "indian rhinoceros": "Indian Rhinoceros",
    "one-horned rhinoceros": "Indian Rhinoceros",
}

# labels too generic to use
IGNORE = {
    "animal", "wildlife", "mammal", "bird", "reptile", "amphibian", "fish", "insect",
    "fauna", "nature", "organism", "vertebrate", "zoo"
}


def normalize(text: str) -> str:
    text = text.lower().strip()
    text = re.sub(r"[^a-z\s]", "", text)
    text = re.sub(r"\s+", " ", text).strip()
    return text


def predict_animal(image_bytes: bytes, db: Session):
    """
    Returns:
      (animal: Animal | None, confidence: float, vision_label: str | None)
    """
    if not image_bytes:
        return None, 0.0, None

    image = vision.Image(content=image_bytes)
    response = client.label_detection(image=image)
    labels = response.label_annotations

    if not labels:
        return None, 0.0, None

    # Try first few labels for better chance
    for lbl in labels[:8]:
        raw_label = lbl.description
        confidence = float(lbl.score)
        norm = normalize(raw_label)

        if not norm or norm in IGNORE:
            continue

        # ✅ 1) Synonyms mapping
        mapped_name = SYNONYMS.get(norm)
        if mapped_name:
            animal = db.query(Animal).filter(Animal.name == mapped_name).first()
            if animal:
                return animal, confidence, raw_label

        # ✅ 2) fallback: try partial match in DB (works nicely for demo)
        animal = db.query(Animal).filter(Animal.name.ilike(f"%{raw_label}%")).first()
        if animal:
            return animal, confidence, raw_label

        animal = db.query(Animal).filter(Animal.name.ilike(f"%{norm}%")).first()
        if animal:
            return animal, confidence, raw_label

    # nothing matched, return top label info
    return None, float(labels[0].score), labels[0].description
