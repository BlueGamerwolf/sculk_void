# Create compound first
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestonePos set value {}

# Set X
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestonePos.X set from storage sculk_void:altar X

# Set Y
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestonePos.Y set from storage sculk_void:altar Y

# Set Z
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestonePos.Z set from storage sculk_void:altar Z

# Tracking + dimension
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestoneTracked set value 1b
execute as @a if data entity @s SelectedItem.tag.altar_compass run data modify entity @s SelectedItem.tag.LodestoneDimension set value "minecraft:overworld"

# REMOVE TAG LAST
execute as @a if data entity @s SelectedItem.tag.altar_compass run data remove entity @s SelectedItem.tag.altar_compass