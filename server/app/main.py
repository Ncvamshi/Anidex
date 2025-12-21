from fastapi import FastAPI
from app.routes.auth import router as auth_router
from app.routes.animals import router as animal_router
from app.routes.predict import router as predict_router

app = FastAPI(title="AniDex Backend")

app.include_router(auth_router)
app.include_router(animal_router)
app.include_router(predict_router)

@app.get("/")
def health():
    return {"status": "AniDex backend running"}
