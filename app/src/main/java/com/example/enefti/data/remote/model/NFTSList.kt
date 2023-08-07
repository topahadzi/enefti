import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.NFTS
import com.google.gson.annotations.SerializedName

data class NFTSList(
    @SerializedName("results") val mList : List<NFTS>

    )