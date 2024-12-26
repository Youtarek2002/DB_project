import requests
import random

BASE_API_URL = "http://localhost:8080/api/authenticate/spot"

def update_spot_status(spot_id, status):
    """Update the status of a parking spot."""
    api_url = f"{BASE_API_URL}/update-spot-status?spotId={spot_id}&status={status.upper()}"
    try:
        response = requests.put(api_url)
        if response.status_code == 200:
            print("Spot status updated successfully:", response.json())
        else:
            print("Failed to update spot status:", response.status_code, response.text)
    except requests.exceptions.RequestException as e:
        print("Error during API request:", e)

def manualChange():
   while True:
        spot_id=input("enter spot id: ")
        print("\n1. AVAILABLE\n2. OCCUPIED\n3. RESERVED\n4. EXIT\n")
        curr = input("Enter the status of the spot: ")
        if curr == "4":
            break
        elif curr not in ["1", "2", "3"]:
            print("Invalid status. Please enter a valid status.")
            continue
        else:
            status = "AVAILABLE" if curr == "1" else "OCCUPIED" if curr == "2" else "RESERVED"
        update_spot_status(spot_id, status)
        
        
def automaticChange():
    for i in range (1, 11):
        spot_id =random.randint(1,10)
        status = random.choice(["AVAILABLE", "OCCUPIED", "RESERVED"])
        update_spot_status(spot_id, status)


def main():
    print("1. Manual Change\n2. Automatic Change")
    choice = input("Enter your choice: ")
    if choice == "1":
        manualChange()
    elif choice == "2":
        automaticChange()
    else:
        print("Invalid choice. Please enter a valid choice.")
        
        

if __name__ == "__main__":
    main()
