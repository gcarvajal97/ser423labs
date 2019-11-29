//
//  PlaceTableViewCell.swift
//  SER423Lab
//
//  Created by Carvajal, Gianni on 11/28/19.
//  Copyright © 2019 Carvajal, Gianni. All rights reserved.
//

import UIKit

class PlaceTableViewCell: UITableViewCell {
    //MARK: Properties
    
    @IBOutlet weak var nameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
