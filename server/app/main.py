from fastapi import FastAPI
from dotenv import load_dotenv
import os

from app.routes.auth import router as auth_router
from app.routes.animals import router as animal_router
from app.routes.predict import router as predict_router
from app.routes.collection import router as collection_router

app = FastAPI(title="AniDex Backend")

load_dotenv()

app.include_router(auth_router)
app.include_router(animal_router)
app.include_router(predict_router)
app.include_router(collection_router)

print("ADC:", os.getenv("GOOGLE_APPLICATION_CREDENTIALS"))


@app.get("/")
def health():
    return {"status": "AniDex backend running"}
