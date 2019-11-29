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
* @version November 28, 2019
*/

import UIKit
import os.log

class PlacesTableViewController: UITableViewController {
    //MARK: Properties
    let jsonStr = """
    {"name" : "ASU-Poly","description" : "Home of ASU's Software Engineering Programs", "category" : "School", "address-title" : "ASU Software Engineering", "address-street" : "7171 E Sonoran Arroyo Mall\\nPeralta Hall 230\\nMesa AZ 85212","elevation" : 1384.0,"latitude" : 33.306388,"longitude" : -111.679121}
    """
    
    var newLocation: PlaceDescription?
    var updateLocation: PlaceDescription?

    override func viewDidLoad() {
        super.viewDidLoad()
        NSLog("Initial Loading")

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        if let path = Bundle.main.path(forResource: "places", ofType: "json") {
            do {
                  let data = try Data(contentsOf: URL(fileURLWithPath: path), options: .mappedIfSafe)
                  let jsonResult = try JSONSerialization.jsonObject(with: data, options: .mutableLeaves)
                  if let jsonResult = jsonResult as? Dictionary<String, AnyObject> {
                    for key in jsonResult.keys {
                        let location = jsonResult[key]
                        let newPlace = PlaceDescription(name: location?["name"] as! String,
                                                        description: location?["description"] as! String,
                                                           category: location?["category"] as! String,
                                                           title: location?["address-title"] as! String,
                                                           street: location?["address-street"] as! String,
                                                           elevation: location?["elevation"] as! Double,
                                                           latitude: location?["latitude"] as! Double,
                                                           longitude: location?["longitude"] as! Double)
                        PlaceLibrary.sharedLibrary.addPlaceToLibrary(placeToAdd: newPlace)
                  }
                }
              } catch {
                   // handle error
              }
        }
        navigationItem.leftBarButtonItem = editButtonItem
    }
    
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            PlaceLibrary.sharedLibrary.removePlace(placeToRemove: PlaceLibrary.sharedLibrary.getLibrary()[indexPath.row])
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return PlaceLibrary.sharedLibrary.getLibrary().count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Table view cells are reused and should be dequeued using a cell identifier.
        let cellIdentifier = "PlaceTableViewCell"
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? PlaceTableViewCell  else {
            fatalError("The dequeued cell is not an instance of PlaceTableViewCell.")
        }
        
        let place = PlaceLibrary.sharedLibrary.getLibrary()[indexPath.row]
        
        cell.nameLabel.text = place.name
        
        return cell
    }

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
            case "AddItem":
                if newLocation != nil {
                    os_log("Adding a new location.", log: OSLog.default, type: .debug)
                }
            
            case "ShowDetail":
                guard let placeDetailViewController = segue.destination as? PlacesDetailViewController else {
                    fatalError("Unexpected destination: \(segue.destination)")
                }
                 
                guard let selectedPlaceCell = sender as? PlaceTableViewCell else {
                    fatalError("Unexpected sender: \(String(describing: sender))")
                }
                 
                guard let indexPath = tableView.indexPath(for: selectedPlaceCell) else {
                    fatalError("The selected cell is not being displayed by the table")
                }
                 
                let selectedLocation = PlaceLibrary.sharedLibrary.getLibrary()[indexPath.row]
                placeDetailViewController.place = selectedLocation
            
            case "UpdateLocation":
                if updateLocation != nil {
                    os_log("Updating location", log: OSLog.default, type: .debug)
                }
            
            default:
                fatalError("Unexpected Segue Identifier; \(String(describing: segue.identifier))")
        }
    }
    
    
    //MARK: Actions
    @IBAction func unwindToPlaceList(sender: UIStoryboardSegue) {
        if let sourceViewController = sender.source as? PlacesAddViewController, let place = sourceViewController.place {
            
            if let selectedIndexPath = tableView.indexPathForSelectedRow {
                // Update an existing meal.
                PlaceLibrary.sharedLibrary.updateLocation(placeToUpdate: updateLocation ?? place)
                tableView.reloadRows(at: [selectedIndexPath], with: .none)
            }
            else {
                // Add a new meal.
                NSLog("Adding Location...")
                let newIndexPath = IndexPath(row: PlaceLibrary.sharedLibrary.getLibrary().count, section: 0)
                
                PlaceLibrary.sharedLibrary.addPlaceToLibrary(placeToAdd: newLocation ?? place)
                tableView.insertRows(at: [newIndexPath], with: .automatic)
            }
        }
       
        
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}
