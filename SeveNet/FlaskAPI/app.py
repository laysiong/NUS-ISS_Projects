from flask import Flask, request, jsonify
import pickle
import re
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import joblib
import nltk
import requests

app = Flask(__name__)

# Ensure the stopwords and wordnet are downloaded
nltk.download('stopwords')
nltk.download('wordnet')

#"D:/LaySiong/NUS_ISS/13-ADProject/app/FlaskAPI/
file_path = ""

# Load the vectorizer and model
with open(file_path + 'spam_detection/tfidf_vectorizer.pkl', 'rb') as f:
    vectorizer = pickle.load(f)

with open(file_path + 'spam_detection/logistic_regression_model.pkl', 'rb') as f:
    model = pickle.load(f)

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json
    text = data['text']
    text_tfidf = vectorizer.transform([text])
    prediction = model.predict(text_tfidf)[0]
    return jsonify({'prediction': 'spam' if prediction == 1 else 'ham'})

# Text preprocessing function
def preprocess_text(text):
    text = re.sub(r'[^A-Za-z\s]', '', text)  # Remove punctuation, numbers, and special characters
    text = text.lower()  # Convert to lowercase
    stop_words = set(stopwords.words('english'))  # Remove stop words
    words = text.split()
    words = [word for word in words if word not in stop_words]
    lemmatizer = WordNetLemmatizer()  # Lemmatization
    words = [lemmatizer.lemmatize(word) for word in words]
    return ' '.join(words)


# Hugging Face Multi Tag API Calls
API_URL = "https://api-inference.huggingface.co/models/unitary/toxic-bert"
headers = {"Authorization": "empty"}

def query_huggingface_api(payload):
    response = requests.post(API_URL, headers=headers, json=payload)
    return response.json()

@app.route('/mlbcheck',methods=['POST'])
def mlbcheck():
    data = request.json
    text = data['text']
    payload = {"inputs": text}
    
    try:
        hf_response = query_huggingface_api(payload)
        if hf_response is not None and isinstance(hf_response, list) and 'label' in hf_response[0][0]:
            filtered_labels = [item['label'] for item in hf_response[0] if item['score'] > 0.2]
            tags_string = ','.join(filtered_labels)  # Convert to comma-separated string
            return tags_string
    except Exception as e:
        return jsonify({"error": str(e)}), 500


try:
    f_tfidf_model = joblib.load(file_path + 'multi_classificaiton/final_model_tfidf.pkl')
    print("f_tfidf_modle loaded successfully")
except Exception as e:
    print(f"Failed to load f_tfidf_modle: {e}")

try:
    f_model = joblib.load(file_path + 'multi_classificaiton/final_model.pkl')
    print("f_model loaded successfully")
except Exception as e:
    print(f"Failed to load f_model: {e}")


# Function to map the predicted output to the labels
def map_prediction_to_labels(prediction):
    label_columns = ['toxic', 'severe_toxic', 'obscene', 'threat', 'insult', 'identity_hate']
    predicted_labels = [label_columns[i] for i in range(len(label_columns)) if prediction[0][i] == 1]
    return predicted_labels

# Define a route for predictions
@app.route('/mlbpredict', methods=['POST'])
def finalpredict():
    data = request.json
    text = data['text']
    # Preprocess the text
    preprocessed_text = preprocess_text(text)    
    text_tfidf = f_tfidf_model.transform([preprocessed_text])

    # Ensure prediction is a 2D array
    prediction = f_model.predict(text_tfidf)
    predicted_labels = map_prediction_to_labels(prediction)
    
    # Return the labels as a comma-separated string or JSON
    return ','.join(predicted_labels)

# Run the Flask app
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
