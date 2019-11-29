/**
* Copyright 2019 Gianni Carvajal
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* @author Gianni Carvajal mailto:gcarvaj3@asu.edu
* @version November 26, 2019
*/

import UIKit
import os.log

class PlacesAddViewController: UIViewController, UITextFieldDelegate, UINavigationControllerDelegate {
    
    var place: PlaceDescription?
    
    @IBOutlet weak var saveNavButton: UIBarButtonItem!    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var streetTextField: UITextField!
    @IBOutlet weak var categoryTextField: UITextField!
    @IBOutlet weak var elevationTextField: UITextField!
    @IBOutlet weak var longitudeTextField: UITextField!
    @IBOutlet weak var latitudeTextField: UITextField!
    @IBOutlet weak var descriptionTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nameTextField.delegate = self
        if let place = place {
            navigationItem.title = "Update Location"
            nameTextField.text = place.name
            titleTextField.text = place.addressTitle
            streetTextField.text = place.addressStreet
            categoryTextField.text = place.category
            elevationTextField.text = String(place.elevation)
            longitudeTextField.text = String(place.longitude)
            latitudeTextField.text = String(place.latitude)
            descriptionTextField.text = place.description
            nameTextField.isUserInteractionEnabled = false
        }
        
        updateSaveButtonState()
        
    }
    
    //MARK: Navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        guard let button = sender as? UIBarButtonItem, button === saveNavButton else {
            os_log("The save button was not pressed, cancelling", log: OSLog.default, type: .debug)
            return
        }
        
        let name = nameTextField.text ?? ""
        let title = titleTextField.text ?? ""
        let street = streetTextField.text ?? ""
        let category = categoryTextField.text ?? ""
        let elevation = elevationTextField.text ?? ""
        let longitude = longitudeTextField.text ?? ""
        let latitude = latitudeTextField.text ?? ""
        let description = descriptionTextField.text ?? ""
        
        guard let elevationAsDouble = Double(elevation) else {
            os_log("Elevation is not a double", log: OSLog.default, type: .debug)
            return
        }
        guard let longitudeAsDouble = Double(longitude) else {
            os_log("Longitude is not a double", log: OSLog.default, type: .debug)
            return
        }
        
        guard let latitudeAsDouble = Double(latitude) else {
            os_log("Elevation is not a double", log: OSLog.default, type: .debug)
            return
        }
        
        let newPlace = PlaceDescription(name: name,
                                        description: description,
                                        category: category,
                                        title: title,
                                        street: street,
                                        elevation: elevationAsDouble,
                                        latitude: latitudeAsDouble,
                                        longitude: longitudeAsDouble)
        guard let placeTableViewController = segue.destination as? PlacesTableViewController else {
            fatalError("Unexpected destination: \(segue.destination)")
        }
        if place != nil {
            placeTableViewController.updateLocation = newPlace
        } else {
            place = newPlace
            placeTableViewController.newLocation = newPlace
        }
        
    }
    
    
    @IBAction func cancel(_ sender: UIBarButtonItem) {
        let isPresentingAddLocation = presentingViewController is PlacesAddViewController
        
        if isPresentingAddLocation {
            dismiss(animated: true, completion: nil)
        } else if let owningNavigationController = navigationController{
            owningNavigationController.popViewController(animated: true)
        } else {
            fatalError("The View Controller is not inside a navigation controller.")
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        NSLog("In viewDidAppear method of add location view")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NSLog("In viewWillAppear method of add location view")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        NSLog("In viewDidDisappear method of add location view")
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("In viewWillDisappear method of add location view")
    }
    
    //MARK: UITextFieldDelegate
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        // Disable the Save button while editing.
        saveNavButton.isEnabled = false
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        updateSaveButtonState()
    }
    
    //MARK: Private Methods
    private func updateSaveButtonState() {
        // Disable the Save button if the text field is empty.
        let text = nameTextField.text ?? ""
        saveNavButton.isEnabled = !text.isEmpty
    }
}
