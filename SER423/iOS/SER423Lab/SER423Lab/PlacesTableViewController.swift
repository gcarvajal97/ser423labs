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
    var urlString: String?
    var placeNames: [String]?
    
    var newLocation: PlaceDescription?
    var updateLocation: PlaceDescription?

    var indicator: UIActivityIndicatorView!
    
    let dispatchQueue = DispatchQueue(label: "Data Queue")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NSLog("Initial Loading")

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        navigationItem.leftBarButtonItem = editButtonItem
        urlString = setURL()
        let indicator = UIActivityIndicatorView(activityIndicatorStyle: .medium)
        self.indicator = indicator
    }
    
    // Initial loading
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        loadingActivity()
    }
    
    // Update table with each action (add, remove, update)
    func loadingActivity() {
        if placeNames == nil {
            self.tableView.reloadData()
            tableView.backgroundView = indicator
            tableView.separatorStyle = .none
            NSLog("In loading activity")
            self.indicator.startAnimating()
            dispatchQueue.async {
                self.callGetNamesNUpdatePlaces()
                Thread.sleep(forTimeInterval: 2)
                OperationQueue.main.addOperation() {
                    self.indicator.stopAnimating()
                    self.tableView.separatorStyle = .singleLine
                    self.tableView.reloadData()
                }
            }
        }
    }
    
    // Handle deleting locations from the list
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            let aConnect:PlaceCollectionStub = PlaceCollectionStub(urlString: urlString!)
            let _:Bool = aConnect.remove(placeName: (self.placeNames?[indexPath.row])!,callback: { _,_  in
                })
            self.placeNames = nil
            self.loadingActivity()
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }

    // MARK: - Table view data source

    // Load the first section
    override func numberOfSections(in tableView: UITableView) -> Int {
        return (placeNames == nil) ? 0 : 1
    }

    // Load the number of rows
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return placeNames?.count ?? 0
    }

    // Load each cell of the table view
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Table view cells are reused and should be dequeued using a cell identifier.
        let cellIdentifier = "PlaceTableViewCell"
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? PlaceTableViewCell  else {
            fatalError("The dequeued cell is not an instance of PlaceTableViewCell.")
        }
        cell.nameLabel?.text = placeNames?[indexPath.row]
        
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
            
            if tableView.indexPathForSelectedRow != nil {
                // Update an existing location.
                PlaceLibrary.sharedLibrary.updateLocation(placeToUpdate: updateLocation ?? place)
                let aConnect:PlaceCollectionStub = PlaceCollectionStub(urlString: urlString!)
                let _:Bool = aConnect.remove(placeName: (updateLocation?.name)!,callback: { _,_  in
                    })
                // Sleep to allow the request to go through before adding the location back
                Thread.sleep(forTimeInterval: 1)
                let _:Bool = aConnect.add(place: updateLocation ?? place,callback: { _,_  in
                    print("\(self.updateLocation?.name ?? place.name) updated as: \(self.updateLocation?.toJsonString() ?? place.toJsonString())")
                self.placeNames = nil
                self.loadingActivity()
                })
                
            } else {
                // Add a new location.
                NSLog("Adding Location...")
                if !(self.placeNames?.contains(newLocation?.name ?? "") ?? true) {
                    let aConnect:PlaceCollectionStub = PlaceCollectionStub(urlString: urlString!)
                    let newPlace:PlaceDescription = PlaceDescription(dict: (newLocation?.toDict())!)
                    let _:Bool = aConnect.add(place: newPlace,callback: { _,_  in
                    print("\(newPlace.name) added as: \(newPlace.toJsonString())")
                    self.placeNames = nil
                    self.loadingActivity()
                    })
                }
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
    
    func setURL () -> String {
        var serverhost:String = "localhost"
        var jsonrpcport:String = "8080"
        var serverprotocol:String = "http"
        // access and log all of the app settings from the settings bundle resource
        if let path = Bundle.main.path(forResource: "ServerInfo", ofType: "plist"){
            // defaults
            if let dict = NSDictionary(contentsOfFile: path) as? [String:AnyObject] {
                serverhost = (dict["server_host"] as? String)!
                jsonrpcport = (dict["server_port"] as? String)!
                serverprotocol = (dict["server_protocol"] as? String)!
            }
        }
        NSLog("setURL returning: \(serverprotocol)://\(serverhost):\(jsonrpcport)")
        return "\(serverprotocol)://\(serverhost):\(jsonrpcport)"
    }
    
    func callGetNamesNUpdatePlaces() {
        PlaceLibrary.sharedLibrary.clearLibrary()
        self.placeNames?.removeAll()
        let aConnect:PlaceCollectionStub = PlaceCollectionStub(urlString: urlString!)
        let _:Bool = aConnect.getNames(callback: { (res: String, err: String?) -> Void in
            if err != nil {
                NSLog(err!)
            } else{
                NSLog(res)
                if let data: Data = res.data(using: String.Encoding.utf8){
                    do{
                        let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:AnyObject]
                        self.placeNames = dict?["result"] as! [String?] as? [String]
                        if (self.placeNames != nil) {
                            for name in self.placeNames! {
                                self.callGetNPopulateUIFields(name)
                            }
                        }
                        
                    } catch {
                        print("unable to convert to dictionary")
                    }
                }
                
            }
        })
        // end of method call to getNames
    }
    
    func callGetNPopulateUIFields(_ name: String){
        let aConnect:PlaceCollectionStub = PlaceCollectionStub(urlString: urlString!)
        let _:Bool = aConnect.get(name: name, callback: { (res: String, err: String?) -> Void in
            if err != nil {
                NSLog(err!)
            }else{
                NSLog(res)
                if let data: Data = res.data(using: String.Encoding.utf8){
                    do{
                        let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:AnyObject]
                        let aDict:[String:AnyObject] = (dict!["result"] as? [String:AnyObject])!
                        let loadedData = PlaceDescription(dict: aDict)
                        PlaceLibrary.sharedLibrary.addPlaceToLibrary(placeToAdd: loadedData)
                    } catch {
                        NSLog("unable to convert to dictionary")
                    }
                }
            }
        })
    }
}
