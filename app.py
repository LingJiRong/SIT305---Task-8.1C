from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Allows Android Emulator to access this server

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json()
    user_msg = data.get("prompt", "")

    # Simple reply logic
    if "hi" in user_msg.lower():
        reply = "Hello! How can I assist you today?"
    elif "bye" in user_msg.lower():
        reply = "Goodbye! See you again!"
    else:
        reply = f"You said: {user_msg}"

    return jsonify({"bot": reply})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
