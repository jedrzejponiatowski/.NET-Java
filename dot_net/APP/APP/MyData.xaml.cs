using System;
using System.Collections.Generic;
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
    /// Interaction logic for MyData.xaml
    /// </summary>
    public partial class MyData : Window
    {
        /// <summary>
        /// Set up when opened. Creates connection to DB
        /// and lists all wallets
        /// </summary>
        public MyData()
        {
            InitializeComponent();
            var context = new PaymentsContext();
            Wallets.ItemsSource = context.TakeAllWallets();
        }

        /// <summary>
        /// Closes window.
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// Changes value of the wallet
        /// </summary>
        /// <param name="sender">Automated parameter from WPF</param>
        /// <param name="e">Automatic parameter. In this situation is NULL</param>
        private void BtnChangeValue(object sender, RoutedEventArgs e)
        {
            var context = new PaymentsContext();
            Wallet wallet = context.GetWalletWithID(1);
            int NewValue = int.Parse(TextBoxValue.Text.Trim());
            context.ChangeWalletValue(wallet, NewValue);
            context.SaveChanges();
            this.Close();
        }
    }
}
