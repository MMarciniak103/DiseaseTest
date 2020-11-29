from fastapi import FastAPI, Query, File, UploadFile, HTTPException
from fastapi.encoders import jsonable_encoder
from fastapi.responses import HTMLResponse,JSONResponse
import pandas as pd
import os
from .initialization import initialize
import numpy as np
import random


app = FastAPI()

init_state = initialize()

diseases,diseases_symptoms,all_symptoms = init_state[0],init_state[1],init_state[2]
print(diseases_symptoms)
diseases_description_df = init_state[3]


@app.get("/")
async def main():
    return "Hello World"

@app.get("/diseases")
async def get_diseases():
    return list(diseases)

@app.get("/symptoms")
async def get_all_symptoms():
    return {"symptoms":list(all_symptoms)}

@app.get("/disease")
async def get_disease_symptoms(disease: str):
    return {"symptoms": list(diseases_symptoms[disease])}

@app.get("/question")
async def get_quiz_symptoms(disease : str):
    disease = disease.strip()
    num_disease_symptoms = len(diseases_symptoms[disease])
    num_of_true = random.randint(0, num_disease_symptoms)
    true_symptoms = random.sample(diseases_symptoms[disease],num_of_true)
    num_of_false = 10 - num_of_true
    all_symptoms_list = list(all_symptoms)
    false_symptoms = []
    i = 0
    while i < num_of_false:
        rnd_symptom = random.choice(all_symptoms_list)
        if rnd_symptom in true_symptoms or rnd_symptom in false_symptoms:
            continue
        false_symptoms.append(rnd_symptom)
        i += 1

    return {"trueSymptoms":true_symptoms,
            "falseSymptoms":false_symptoms}

@app.get("/disease/description")
async def get_disease_description(disease: str):
    disease.strip()
    description = diseases_description_df[diseases_description_df['Disease'].str.strip()==disease]['Description'].values[0]
    return description