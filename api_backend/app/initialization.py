import os
import pandas as pd

def initialize():
    df = pd.read_csv(os.path.join(data_path, 'dataset.csv'))
    # find all unique diseases
    diseases = get_unique_diseases(df)
    # get unique symptoms for diseases
    

    all_symptoms = set()
    for i in range(1, 18):
        for symptom in df[f"Symptom_{i}"].unique():
            if pd.isnull(symptom):
                continue
            all_symptoms.add(symptom.strip())


def get_unique_diseases(df):
    return df['Disease'].unique()

def get_diseases_symptoms(df,diseases):
    diseases_symptoms = {}
    for disease in diseases:
        diseases_symptoms[disease] = set()
        for i in range(1, 18):
            for symptom in df[df['Disease'] == disease][f"Symptom_{i}"].unique():
                if pd.isnull(symptom):
                    continue
                diseases_symptoms[disease].add(symptom.strip())
    return diseases_symptoms