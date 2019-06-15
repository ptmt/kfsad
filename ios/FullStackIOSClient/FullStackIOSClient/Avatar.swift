//
//  Avatar.swift
//  FullStackIOSClient
//
//  Created by Dmitriy L on 6/13/19.
//  Copyright © 2019 JetBrains. All rights reserved.
//

import SwiftUI
import URLImage

struct Avatar : View {
    var imageUrl: String
    
    var body: some View {
        URLImage(URL(string: imageUrl)!)
            .frame(width: 50, height: 50)
            .cornerRadius(4)
            .clipped()
            .shadow(radius: 50)
    }
}

#if DEBUG
struct Avatar_Previews : PreviewProvider {
    static var previews: some View {
        Avatar(imageUrl: "https://i.pravatar.cc/100?u=Maxime_Nienow&dpr=2&size=100")
    }
}
#endif

