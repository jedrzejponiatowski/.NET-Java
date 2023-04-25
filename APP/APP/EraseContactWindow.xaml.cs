using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace APP
{
    /// <summary>
    /// Logika interakcji dla klasy EraseContactWindow.xaml
    /// </summary>
    public partial class EraseContactWindow : Window
    {
        public EraseContactWindow()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            ContactsList2.ItemsSource = context.TakeAllPersons();
        }

        private void BtnEraseContact(object sender, RoutedEventArgs e)
        {
            string pp = (string) ContactsList2.SelectedItems[0];
            string IDstring = new string(pp.TakeWhile(Char.IsDigit).ToArray());
            int ID = int.Parse(IDstring);

            var context = new PaymentsContext();
            context.DeletePerson( context.GetPersonWithID(ID));
            this.Close();
        }

        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
