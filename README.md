![cd8a9f4c4392311d995fa8ae2302c0fa](https://github.com/changlaplace/EntityXRay-and-AutoAim/assets/105801175/e24189d5-a1a6-453b-96dd-28335a54c539)
# Entity auto-Aiming and XRay (Forge Edition for Minecraft 1.19.1)

Just like Advanced-XRay Mod but for entities. I learned a lot from that code and because of laziness I build the two mod together so that you may find XRay mod in it.

Now the Autoaim entities function is newly added,btw thanks for the aimassistant mod where I learned a lot.But this mod is more suitable for gun fighting(like zombies game).
![Forge Downloads](./.github/assets/show1.png)
![Forge Downloads](./.github/assets/show2.png)
![Forge Downloads](./.github/assets/show3.png)

## Key binding: 
H for GUI and J for toggling on/off the entity xray(you can also toggle it in the gui),autoAim settings is in the gui too

## Todo:
I want to build entityXray like Entityoutliner mod but feel ignornant when comes to OpenGl rendering.Maybe I will spend some time learning things but not playing with mods.

## Settings explained for autoAim
HEADSHOT POSITION is the height where you want to aim at an entity.1 means aim at the eye position, and More means higher.

SCROLLING SPEED is the speed of player's rotation to aim at an entity.Usually below 30 is enough cuz too larger the value will make it hard to move the crosshair.

ANGLEOFSEARCH defines the field of view for detecting aiming entities.Once zombies are found within this angle.The mod will help u aim at the closest one.I hardcoded
the near field angleofsearch to be 45 which can only be modified from the code.I think it's good for fighting closer zombies.

DELAYFRAME is how many frames does it delay on certain server.It ususlly is set below 5.For example,I ping high for hypixel so I set it to 1~2.It can amend the aiming error caused by the lag.

## Hope you enjoy pve games with this mod
