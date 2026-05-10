salut!
   Acesta este un scrip pentru gasirea hotelurilor dintr-o anumita locatie. 
   Datele de intrare vor fi scrise in fisierul input.txt din directorul input
city=Vienna
check_in=2026-07-15
check_out=2026-07-17
adults=2
children=2
children_ages=5,5
currency=EUR
   sub aceasta forma, primele 3 campuri sunt obligatorii!!
   La iesire se obtine un fisier JSON cu datele sub aceasta forma:
   hotel["name"],
   hotel["rate_per_night"],
   hotel["total_rate"],
   hotel["link"],
   hotel["rating"],
   hotel["nearby_places"]
	{
		nearby_places["name"],
		nearby_places["transporations"]
			{
				transporations["type"],
				transporations["duration"]
			}
	}

   In final, acest fisier JSON este transformat in directorul ouput intr-o baza de date baza.db
   care contine 3 tabele separate. hotels--> nearby_places --> transporation
	
											       
   
	