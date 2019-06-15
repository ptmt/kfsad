import SwiftUI
import SharedLib

struct PostRow : View {
    let post: Post
    let user: User
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Avatar(imageUrl: "https://i.pravatar.cc/56?u=Maxime_Nienow&dpr=2&size=56").padding(4.0)
                VStack(alignment: .leading) {
                    Text(post.title).font(.headline)
                    Text(user.name).font(.subheadline)
                }
            }
            
            Text(post.body)
                .lineLimit(4)
                .font(.caption)
                .padding(3.0)
            
    }.padding(10.0).cornerRadius(6.0).background(Color.white.cornerRadius(15))
    }
}

#if DEBUG
func makePost() -> Post {
    Post(userId: 1, id: 1, title: "Title For Preview", body: "By reason of these things, then, the whaling voyage was welcome; the great flood-gates of the wonder-world swung open, and in the wild conceits that swayed me to my purpose, two and two there floated into my inmost soul, endless processions of the whale,")
}

func makeUser() -> User {
    User(id: 1, name: "User Name", username: "Maxime_Nienow", email: "na", address: UserAddress(street: "na", suite: "na", city: "na", zipcode: "na", geo: Geo(lat: "na", lng: "na")), phone: "na", website: "na", company: UserCompany(name: "na", catchPhrase: "na", bs: "na"))
}
struct PostRow_Previews : PreviewProvider {
    static var previews: some View {
        PostRow(
            post: makePost(),
            user: makeUser()
        ).previewLayout(.fixed(width: 340, height: 200))
    }
}
#endif
