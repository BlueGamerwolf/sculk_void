# Store X
execute as @e[tag=altar_marker,limit=1] at @s store result storage sculk_void:altar X int 1 run data get entity @s Pos[0]

# Store Y
execute as @e[tag=altar_marker,limit=1] at @s store result storage sculk_void:altar Y int 1 run data get entity @s Pos[1]

# Store Z
execute as @e[tag=altar_marker,limit=1] at @s store result storage sculk_void:altar Z int 1 run data get entity @s Pos[2]