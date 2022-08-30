package za.co.kasid.kasidrestapi.model.message_recieved

data class Contact(
    var profile: Profile = Profile(),
    var wa_id: String = ""
)