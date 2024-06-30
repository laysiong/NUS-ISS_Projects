using System.Transactions;
using System;

namespace CA_Project_Activation_Codes.Models;

public class Cart_Prod:Product
{
    public string TranId { get; set; }
    public int ProdQuantity { get; set; }


    //public string test()
    //{
    //    return "i got here";
    //}

}
