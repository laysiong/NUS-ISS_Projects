namespace CA_Project_Activation_Codes.Models;

public class Prod_Purchased:Cart_Prod
{
    public string Dates { get; set; }

    //this have to be List because it might have multiple products
    public List<string> Codes { get; set; }

    public Prod_Purchased()
    {
        Codes = new List<string>();
    }

    public void Add(string code)
    {
        Codes.Add(code);
    }


}

