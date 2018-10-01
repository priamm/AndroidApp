package com.enecuumwallet.androidapp.presentation.presenter.balance

//    {"jsonrpc":"2.0","params":{"address":"B026nJ7HB2nPtPjxkTrH2V5PzhgsWWab1gpi29oLsfiTKv"},"method":"enq_getBalance","id":1}
//    {"result":0,"jsonrpc":"2.0","id":1}
data class Params(val params: Address, val method: String = "enq_getBalance")

data class Address(val data: String)