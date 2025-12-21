from google.cloud import vision
import io

# Initialize Vision client once
client = vision.ImageAnnotatorClient()

# Map Google Vision labels to YOUR DB animal names
LABEL_MAP = {
    "Tiger": "Bengal Tiger",
    "Elephant": "Indian Elephant",
    "Dog": "Indian Pariah Dog",
    "Peacock": "Indian Peafowl",
    "Leopard": "Leopard"
}

def predict_animal(image_path: str):
    """
    Takes an image path
    Returns: (mapped_animal_name, confidence)
    """

    with io.open(image_path, "rb") as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    response = client.label_detection(image=image)
    labels = response.label_annotations

    if not labels:
        return "Unknown", 0.0

    # Take the top label
    top_label = labels[0].description
    confidence = labels[0].score

    # Map to DB name
    mapped_name = LABEL_MAP.get(top_label, "Unknown")

    return mapped_name, float(confidence)
