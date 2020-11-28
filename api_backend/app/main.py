from fastapi import FastAPI, Query, File, UploadFile, HTTPException
from fastapi.encoders import jsonable_encoder
from fastapi.responses import HTMLResponse,JSONResponse
import pandas as pd
import os

app = FastAPI()

data_path = os.path.join('./'+os.getcwd(),'data')

@app.get("/")
async def main():
    return "Hello World"

@app.get("/diseases")
async def get_diseases():
    return {"diseases":list(diseases)}


