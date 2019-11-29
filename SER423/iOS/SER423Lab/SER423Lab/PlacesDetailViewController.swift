//
//  PlacesDetailViewController.swift
//  SER423Lab
//
//  Created by Carvajal, Gianni on 11/29/19.
//  Copyright © 2019 Carvajal, Gianni. All rights reserved.
//

import UIKit

class PlacesDetailViewController: UIViewController {

    @IBOutlet weak var detailLabel: UILabel!
    var place: PlaceDescription?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        detailLabel.text = place?.toString() ?? "No Location Set"
        detailLabel.lineBreakMode = .byWordWrapping
        detailLabel.numberOfLines = 0
        title = place?.name
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        super.prepare(for: segue, sender: sender)
        guard let placeUpdateViewController = segue.destination as? PlacesAddViewController else {
                return
        }
         
        placeUpdateViewController.place = self.place
    }
    
    @IBAction func back(_ sender: UIBarButtonItem) {
        dismiss(animated: true, completion: nil)
    }
    
    
}
